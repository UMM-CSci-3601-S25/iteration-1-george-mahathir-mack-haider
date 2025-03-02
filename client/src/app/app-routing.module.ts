import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { AddUserComponent } from './users/add-user.component';
import { UserListComponent } from './users/user-list.component';
import { UserProfileComponent } from './users/user-profile.component';
import { CompanyListComponent } from './company-list/company-list.component';
import { HostComponent } from './hosts/host-list.component';
import { PlayerComponent } from './players/player-list.component';
import { HostSettingsComponent } from './hosts/host-setting.component';
import { GameComponent } from './game/game.component';

const randomString: string = generateRandomString(5); // Generates a 5-character random string

// Note that the 'users/new' route needs to come before 'users/:id'.
// If 'users/:id' came first, it would accidentally catch requests to
// 'users/new'; the router would just think that the string 'new' is a user ID.
const routes: Routes = [
  {path: '', component: HomeComponent, title: 'Home'},
  {path: 'host', component: HostComponent, title: 'Host'},
  {path: 'player', component: PlayerComponent, title: 'Player'},
  {path: 'host/settings', component: HostSettingsComponent, title: 'Host Settings'},
  {path: 'game', component: GameComponent, title: 'randomString'},

  {path: 'users', component: UserListComponent, title: 'Users'},
  {path: 'users/new', component: AddUserComponent, title: 'Add User'},
  {path: 'users/:id', component: UserProfileComponent, title: 'User Profile'},
  {path: 'companies', component: CompanyListComponent, title: 'Companies'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

function generateRandomString(length: number): string {
  const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
  let result = '';
  for (let i = 0; i < length; i++) {
    result += characters.charAt(Math.floor(Math.random() * characters.length));
  }
  return result;
}
