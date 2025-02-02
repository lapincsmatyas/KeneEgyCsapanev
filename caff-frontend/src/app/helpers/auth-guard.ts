import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { TokenStorageService } from '../services/token-storage.service';
import {AuthService} from "../services/auth.service";

@Injectable()
export class AuthGuard implements CanActivate {


  constructor(private authService: AuthService, private _router: Router) {
  }

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    if (this.authService.isLoggedIn()) {
        return true;
    }
    this._router.navigate(['/login']);
    return false;
  }

}
