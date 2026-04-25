import { Component, OnInit, inject, signal, AfterViewInit, ElementRef, Renderer2 } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import SharedModule from 'app/shared/shared.module';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  standalone: true,
  imports: [SharedModule, RouterModule, CommonModule],
})
export default class HomeComponent implements OnInit, AfterViewInit {
  account = signal<Account | null>(null);
  private readonly accountService = inject(AccountService);
  private readonly router = inject(Router);
  private readonly renderer = inject(Renderer2);
  private readonly elementRef = inject(ElementRef);

  ngOnInit(): void {
    this.accountService.getAuthenticationState().subscribe(account => this.account.set(account));
  }

  ngAfterViewInit(): void {
    this.initScrollReveal();
    this.initNavbarGlass();
  }

  private initScrollReveal(): void {
    const reveals = this.elementRef.nativeElement.querySelectorAll('.scroll-reveal');
    const observer = new IntersectionObserver(
      entries => {
        entries.forEach(entry => {
          if (entry.isIntersecting) {
            this.renderer.addClass(entry.target, 'is-visible');
            observer.unobserve(entry.target);
          }
        });
      },
      { threshold: 0.15, rootMargin: '0px 0px -50px 0px' },
    );
    reveals.forEach((el: Element) => observer.observe(el));
  }

  private initNavbarGlass(): void {
    const nav = document.getElementById('mainNav');
    if (!nav) return;

    const onScroll = () => {
      if (window.scrollY > 50) {
        this.renderer.addClass(nav, 'glass');
      } else {
        this.renderer.removeClass(nav, 'glass');
      }
    };

    onScroll(); // init
    window.addEventListener('scroll', onScroll, { passive: true });

    // Cleanup si nécessaire (optionnel)
    // return () => window.removeEventListener('scroll', onScroll);
  }

  login(): void {
    this.router.navigate(['/login']);
  }
}
