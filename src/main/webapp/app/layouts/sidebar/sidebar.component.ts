// src/main/webapp/app/layouts/sidebar/sidebar.component.ts
import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { NgbCollapseModule } from '@ng-bootstrap/ng-bootstrap';
import SharedModule from 'app/shared/shared.module';
import { AccountService } from 'app/core/auth/account.service';
import { EntityNavbarItems } from 'app/entities/entity-navbar-items';
import NavbarItem from '../navbar/navbar-item.model';

@Component({
  selector: 'jhi-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
  imports: [RouterModule, SharedModule, NgbCollapseModule],
})
export default class SidebarComponent implements OnInit {
  isSidebarCollapsed = signal(false);
  isMobileMenuOpen = signal(false);
  account = inject(AccountService).trackCurrentAccount();
  entitiesNavbarItems: NavbarItem[] = [];

  // État des sous-menus
  expandedMenus = signal<Set<string>>(new Set());

  private readonly router = inject(Router);
  private readonly translateService = inject(TranslateService);

  // Regrouper les entités par catégorie
  menuGroups = computed(() => {
    const items = this.entitiesNavbarItems;
    return {
      network: items.filter(item => ['operateur', 'ligne', 'arret', 'ligne-arret'].includes(item.route.substring(1))),
      vehicles: items.filter(item => ['bus', 'tracking'].includes(item.route.substring(1))),
      users: items.filter(item => ['utilisateur', 'alerte', 'notification', 'favori'].includes(item.route.substring(1))),
    };
  });

  ngOnInit(): void {
    this.entitiesNavbarItems = EntityNavbarItems;
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
  }
}
