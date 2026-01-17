import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ClientListComponent } from './client-list.component';
import { ClientService } from '../../core/services/client.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { COMMON_TEST_IMPORTS } from '../../../tests/test-imports';

describe('ClientListComponent', () => {
  let component: ClientListComponent;
  let fixture: ComponentFixture<ClientListComponent>;
  let clientServiceSpy: jasmine.SpyObj<ClientService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    clientServiceSpy = jasmine.createSpyObj('ClientService', ['getClients']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    clientServiceSpy.getClients.and.returnValue(of([]));

    await TestBed.configureTestingModule({
      imports: [
        ClientListComponent,
        ...COMMON_TEST_IMPORTS
      ],
      providers: [
        { provide: ClientService, useValue: clientServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ClientListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges(); // triggers ngOnInit
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load clients on init', () => {
    expect(clientServiceSpy.getClients).toHaveBeenCalled();
    expect(component.dataSource.data.length).toBe(0);
  });

  it('should navigate to client details', () => {
    component.viewDetails({ id: '123' } as any);
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/rm/client', '123']);
  });

  it('should logout and navigate to login', () => {
    spyOn(localStorage, 'clear');
    component.logout();
    expect(localStorage.clear).toHaveBeenCalled();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
  });
});
