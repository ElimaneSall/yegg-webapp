// src/main/webapp/app/layouts/sidebar/sidebar.component.ts
import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { NgbCollapseModule, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import SharedModule from 'app/shared/shared.module';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/login/login.service';
import { EntityNavbarItems } from 'app/entities/entity-navbar-items';
import NavbarItem from '../navbar/navbar-item.model';
import LoginComponent from '../../login/login.component';

@Component({
  selector: 'jhi-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
  standalone: true,
  imports: [RouterModule, SharedModule, NgbCollapseModule, FaIconComponent],
})
export default class SidebarComponent implements OnInit {
  isSidebarCollapsed = signal(false);
  isMobileMenuOpen = signal(false);
  account = inject(AccountService).trackCurrentAccount();
  entitiesNavbarItems: NavbarItem[] = [];

  // État des sous-menus
  expandedMenus = signal<Set<string>>(new Set(['network', 'vehicles', 'users', 'alerts', 'reports']));

  private readonly router = inject(Router);
  private readonly loginService = inject(LoginService);

  // Regrouper les entités par catégorie
  menuGroups = computed(() => {
    const items = this.entitiesNavbarItems;

    return {
      network: items.filter(item => ['operateur', 'ligne', 'arret', 'ligne-arret'].includes(item.route.substring(1))),
      vehicles: items.filter(item => ['bus', 'tracking', 'maps-views'].includes(item.route.substring(1))),
      users: items.filter(item => ['utilisateur', 'favori'].includes(item.route.substring(1))),
      alerts: items.filter(item =>
        ['alerte-approche', 'alerte-ligne-arret', 'historique-alerte', 'notification'].includes(item.route.substring(1)),
      ),
      feedback: items.filter(item => ['feedback', 'rapport'].includes(item.route.substring(1))),
    };
  });

  // Vérifier si l'utilisateur a le rôle ADMIN
  hasAdminAuthority = computed(() => {
    const accountValue = this.account();
    return accountValue?.authorities?.includes('ROLE_ADMIN') ?? false;
  });

  // Vérifier si l'utilisateur a le rôle OPERATOR
  hasOperatorAuthority = computed(() => {
    const accountValue = this.account();
    return accountValue?.authorities?.includes('ROLE_OPERATOR') ?? false;
  });

  // Vérifier si l'utilisateur a le rôle DRIVER
  hasDriverAuthority = computed(() => {
    const accountValue = this.account();
    return accountValue?.authorities?.includes('ROLE_DRIVER') ?? false;
  });

  // Obtenir le rôle affichable
  userRole = computed(() => {
    const accountValue = this.account();
    if (!accountValue) return '';

    if (accountValue.authorities?.includes('ROLE_ADMIN')) return 'Administrateur';
    if (accountValue.authorities?.includes('ROLE_OPERATOR')) return 'Opérateur';
    if (accountValue.authorities?.includes('ROLE_DRIVER')) return 'Chauffeur';
    return 'Passager';
  });

  // Obtenir le nom complet de l'utilisateur
  userFullName = computed(() => {
    const accountValue = this.account();
    if (!accountValue) return '';

    if (accountValue.firstName && accountValue.lastName) {
      return `${accountValue.firstName} ${accountValue.lastName}`;
    } else if (accountValue.firstName) {
      return accountValue.firstName;
    } else if (accountValue.lastName) {
      return accountValue.lastName;
    } else if (accountValue.login) {
      return accountValue.login;
    }
    return '';
  });

  ngOnInit(): void {
    this.entitiesNavbarItems = EntityNavbarItems;

    // Ouvrir certains menus par défaut si l'utilisateur a certains rôles
    const expanded = new Set(this.expandedMenus());
    if (this.hasAdminAuthority()) {
      expanded.add('admin');
    }
    this.expandedMenus.set(expanded);
  }

  toggleSidebar(): void {
    this.isSidebarCollapsed.update(val => !val);
  }

  toggleMobileMenu(): void {
    this.isMobileMenuOpen.update(val => !val);
  }

  toggleSubmenu(menuKey: string): void {
    const expanded = new Set(this.expandedMenus());
    if (expanded.has(menuKey)) {
      expanded.delete(menuKey);
    } else {
      expanded.add(menuKey);
    }
    this.expandedMenus.set(expanded);
  }

  isSubmenuExpanded(menuKey: string): boolean {
    return this.expandedMenus().has(menuKey);
  }

  isActiveRoute(route: string): boolean {
    return this.router.isActive(route, {
      paths: 'exact',
      queryParams: 'ignored',
      fragment: 'ignored',
      matrixParams: 'ignored',
    });
  }

  closeMobileMenu(): void {
    this.isMobileMenuOpen.set(false);
    if (window.innerWidth < 768) {
      this.isSidebarCollapsed.set(true);
    }
  }

  /**
   * Ouvre la modale de connexion
   */
  login(): void {
    // this.loginModalService.open();
  }

  /**
   * Déconnecte l'utilisateur
   */
  logout(): void {
    this.loginService.logout();
    this.router.navigate(['/']).then(() => {
      // Recharger la page pour nettoyer l'état
      window.location.reload();
    });
  }

  /**
   * Vérifie si un élément de menu doit être visible selon les autorisations
   */
  isMenuItemVisible(item: NavbarItem): boolean {
    if (!item.authority) {
      return true;
    }

    const accountValue = this.account();
    if (!accountValue) {
      return false;
    }

    if (Array.isArray(item.authority)) {
      return item.authority.some(auth => accountValue.authorities?.includes(auth));
    }

    return accountValue.authorities?.includes(item.authority) ?? false;
  }
}
