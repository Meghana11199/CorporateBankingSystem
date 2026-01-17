import { Component, EventEmitter, Output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-analyst-header',
  standalone: true,
  imports: [MatButtonModule],
  templateUrl: './analyst-header.component.html',
styleUrls: ['./analyst-header.component.css']
})
export class AnalystHeaderComponent {
  @Output() logout = new EventEmitter<void>();
}