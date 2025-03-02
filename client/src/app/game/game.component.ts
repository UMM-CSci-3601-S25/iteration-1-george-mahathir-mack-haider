import { Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { MatGridListModule} from '@angular/material/grid-list';
import { MatIconModule } from '@angular/material/icon';


@Component({
  selector: 'app-game-component',
  templateUrl: 'game.component.html',
  styleUrls: ['./game.component.scss'],
  providers: [],
  imports: [
    MatCardModule,
    MatButtonModule,
    MatGridListModule,
    RouterLink,
    MatIconModule,
  ]
})

export class GameComponent {

}

export class Colors {
  purple: string = '#800080';
  blue: string = '#0000FF';
  green: string = '#008000';
  red: string = '#FF0000';
  black: string = '#000000';
  white: string = '#FFFFFF';
}
