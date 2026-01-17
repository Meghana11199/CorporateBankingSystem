import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'banking-frontend';
}


// import { HttpClientModule } from '@angular/common/http';
// import { Component, signal } from '@angular/core';
// import { BrowserModule } from '@angular/platform-browser';
// import { RouterOutlet } from '@angular/router';

// @Component({
//   selector: 'app-root',
//    standalone: true,
//   imports: [RouterOutlet],
//   templateUrl: './app.html',
//   styleUrl: './app.css'
// })
// export class App {
//   protected readonly title = signal('corporate-banking-frontend');
// }
