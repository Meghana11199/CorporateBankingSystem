import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ClientDetailsComponent } from './client-details.component';
import { ClientService } from '../../core/services/client.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { COMMON_TEST_IMPORTS, MOCK_ACTIVATED_ROUTE } from '../../../tests/test-imports';

describe('ClientDetailsComponent', () => {
  let component: ClientDetailsComponent;
  let fixture: ComponentFixture<ClientDetailsComponent>;
  let clientServiceSpy: jasmine.SpyObj<ClientService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    clientServiceSpy = jasmine.createSpyObj('ClientService', ['getClientById']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    clientServiceSpy.getClientById.and.returnValue(
      of({ id: '123', companyName: 'ABC Corp' } as any)
    );

    await TestBed.configureTestingModule({
      imports: [
        ClientDetailsComponent,
        ...COMMON_TEST_IMPORTS
      ],
      providers: [
        MOCK_ACTIVATED_ROUTE,
        { provide: ClientService, useValue: clientServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ClientDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges(); // ngOnInit
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load client using route id', () => {
    expect(clientServiceSpy.getClientById).toHaveBeenCalledWith('123');
    expect(component.loading).toBeFalse();
  });

  it('should handle API error', () => {
    clientServiceSpy.getClientById.and.returnValue(
      throwError(() => new Error('fail'))
    );

    component.ngOnInit();

    expect(component.errorMsg).toBe('Failed to load client details');
    expect(component.loading).toBeFalse();
  });

  it('should navigate back to client list', () => {
    component.goBack();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/rm/clients']);
  });

  it('should logout and navigate to login', () => {
    spyOn(localStorage, 'clear');
    component.logout();

    expect(localStorage.clear).toHaveBeenCalled();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
  });
});
