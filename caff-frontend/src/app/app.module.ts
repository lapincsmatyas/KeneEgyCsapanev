import { BrowserModule } from '@angular/platform-browser';
import {  NgModule } from '@angular/core';
import { HttpClientModule } from "@angular/common/http";
import { RouterModule, Routes } from "@angular/router";
import { FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatToolbarModule} from '@angular/material/toolbar';
import { MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon"

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LoginComponent } from './components/login/login.component';
import { HeaderComponent } from './components/header/header.component';

import { RegisterComponent } from './components/register/register.component';
import { authInterceptorProviders } from './helpers/auth.interceptor';
import {CaffListComponent} from "./components/caff-list/caff-list.component";
import { CaffComponent } from './components/caff/caff.component';
import { AuthGuard } from './helpers/auth-guard';
import { CartComponent } from './components/cart/cart.component';
import { ProfileComponent } from './components/profile/profile.component';
import { CommonModule } from '@angular/common';
import { AdminComponent } from './components/admin/admin.component';
import {ToastrModule} from "ngx-toastr";
import {AdminGuard} from "./helpers/admin-guard";

const routers: Routes = [
  {path: 'header', component: HeaderComponent},
  { path: 'login', component: LoginComponent},
  { path: 'register', component: RegisterComponent },

  { path: 'profile/:username', component: ProfileComponent, canActivate: [AuthGuard]},
  { path: 'admin', component: AdminComponent, canActivate: [AdminGuard]},

  { path: 'caffs', component: CaffListComponent, canActivate: [AuthGuard]},
  { path: 'caff/:id', component: CaffComponent, canActivate: [AuthGuard]},
  { path: '**', redirectTo: "caffs"}
];

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    LoginComponent,
    RegisterComponent,
    CaffListComponent,
    CaffComponent,
    CartComponent,
    ProfileComponent,
    AdminComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot(routers),
    FormsModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    ReactiveFormsModule,
    MatButtonModule,
    CommonModule,
    ToastrModule.forRoot(), // ToastrModule added
  ],
  providers: [authInterceptorProviders, AuthGuard, AdminGuard],
  bootstrap: [AppComponent]
})
export class AppModule { }

