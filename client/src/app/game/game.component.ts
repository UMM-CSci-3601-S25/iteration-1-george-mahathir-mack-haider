import { Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
//import { RouterLink } from '@angular/router';
import { environment } from '../../environments/environment';
import { MatGridListModule} from '@angular/material/grid-list';
import { MatIconModule } from '@angular/material/icon';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Response } from './response';
import { map } from 'rxjs/operators';





@Component({
  selector: 'app-game-component',
  templateUrl: 'game.component.html',
  styleUrls: ['./game.component.scss'],
  providers: [],
  imports: [
    MatCardModule,
    MatButtonModule,
    MatGridListModule,
    MatIconModule,
  ]
})

export class GameComponent {

  readonly hostUrl: string = `${environment.apiUrl}prompts`;

  constructor(private httpClient: HttpClient) {
  }

  generateRandomID(length: number): string {
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    let result = '';
    for (let i = 0; i < length; i++) {
      result += characters.charAt(Math.floor(Math.random() * characters.length));
    }
    return result;
  }

  getResponse(): Observable<Response[]> {
    return this.httpClient.get<Response[]>(this.hostUrl);
  }

  addResponse(newResponse: Partial<Response>): Observable<string> {
    return this.httpClient.post<{id: string}>(this.hostUrl, newResponse).pipe(map(response => response.id));
  }




}

