ng new user-auth-app --standalone --routing --style=css
cd user-auth-app
code .
ng generate component pages/register --standalone
ng generate component pages/login --standalone
ng generate component pages/profile --standalone
ng generate service services/auth



// main.ts

import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter, Routes } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { AppComponent } from './app/app.component';
import { RegisterComponent } from './app/pages/register/register.component';
import { LoginComponent } from './app/pages/login/login.component';
import { ProfileComponent } from './app/pages/profile/profile.component';

const routes: Routes = [
  { path: '', redirectTo: 'register', pathMatch: 'full' },
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  { path: 'profile', component: ProfileComponent },
];

bootstrapApplication(AppComponent, {
  providers: [provideRouter(routes), provideHttpClient()],
});



//service.auth.ts

import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly USER_KEY = 'auth_user';

  register(user: any) {
    localStorage.setItem(this.USER_KEY, JSON.stringify(user));
  }

  login(email: string, password: string): boolean {
    const user = this.getUser();
    return user && user.email === email && user.password === password;
  }

  getUser() {
    const data = localStorage.getItem(this.USER_KEY);
    return data ? JSON.parse(data) : null;
  }

  isLoggedIn(): boolean {
    return !!this.getUser();
}
}


// resgiter.component.ts

import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  form: FormGroup;

  constructor(private fb: FormBuilder, private auth: AuthService, private router: Router) {
    this.form = this.fb.group({
      name: '',
      email: '',
      password: ''
    });
  }

  submit() {
    this.auth.register(this.form.value);
    alert('Registered Successfully!');
    this.router.navigate(['/login']);
}
}


//register.component.html

<div class="auth-container">
    <h2>Register</h2>
    <form [formGroup]="form" (ngSubmit)="submit()">
      <input formControlName="name" placeholder="Name" required />
      <input formControlName="email" placeholder="Email" required type="email" />
      <input formControlName="password" placeholder="Password" required type="password" />
      <button type="submit">Register</button>
      <p>Already have an account? <a routerLink="/login">Login</a></p>
  </form>
  </div>


  //register.component.css

  .auth-container {
    max-width: 400px;
    margin: auto;
    padding: 2rem;
    border: 1px solid #ccc;
    border-radius: 10px;
    text-align: center;
  }
  
  input, button {
    display: block;
    width: 100%;
    margin-bottom: 1rem;
    padding: 0.5rem;
  }


  //profile.component.ts

  import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent {
  user: any;

  constructor(private auth: AuthService) {
    this.user = this.auth.getUser();
}
}

//profile.component.html

<div class="profile-container" *ngIf="user">
    <h2>Welcome, {{ user.name }}</h2>
    <p><strong>Email:</strong> {{ user.email }}</p>
    <a routerLink="/login">Logout</a>
  </div>


  //profile.component.css

  .profile-container {
    max-width: 400px;
    margin: auto;
    padding: 2rem;
    text-align: center;
    background: #f7f7f7;
    border-radius:10px;
  }

  //login.component.ts

  import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  form: FormGroup;

  constructor(private fb: FormBuilder, private auth: AuthService, private router: Router) {
    this.form = this.fb.group({
      email: '',
      password: ''
    });
  }

  submit() {
    const { email, password } = this.form.value;
    if (this.auth.login(email!, password!)) {
      alert('Login Successful!');
      this.router.navigate(['/profile']);
    } else {
      alert('Invalid credentials');

    }
}
}

//login.component.html

<div class="auth-container">
    <h2>Login</h2>
    <form [formGroup]="form" (ngSubmit)="submit()">
      <input formControlName="email" placeholder="Email" required />
      <input formControlName="password" placeholder="Password" required type="password" />
      <button type="submit">Login</button>
      <p>Don't have an account? <a routerLink="/register">Register</a></p>
  </form>
  </div>

  

// login.component.css

.auth-container {
    max-width: 400px;
    margin: auto;
    padding: 2rem;
    border: 1px solid #ccc;
    border-radius: 10px;
    text-align: center;
  }






  
  //app.component.ts

  import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  template: `<router-outlet></router-outlet>`,
})
export class AppComponent{}