import { Component, OnInit, inject, signal } from '@angular/core';
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
export default class HomeComponent implements OnInit {
  account = signal<Account | null>(null);
  private readonly accountService = inject(AccountService);
  private readonly router = inject(Router);

  ngOnInit(): void {
    this.accountService.getAuthenticationState().subscribe(account => this.account.set(account));
  }

  login(): void {
    this.router.navigate(['/login']);
  }
}
