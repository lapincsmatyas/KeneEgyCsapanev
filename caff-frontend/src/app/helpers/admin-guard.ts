import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {Observable} from 'rxjs';
import {TokenStorageService} from '../services/token-storage.service';
import {AuthService} from "../services/auth.service";

@Injectable()
export class AdminGuard implements CanActivate {
  constructor(private authService: AuthService, private _router: Router) {
  }

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    if (this.authService.isAdmin()) {
      return true;
    }

    // navigate to login page
    this._router.navigate(['/caffs']);
    // you can save redirect url so after authing we can move them back to the page they requested
    return false;
  }

}
