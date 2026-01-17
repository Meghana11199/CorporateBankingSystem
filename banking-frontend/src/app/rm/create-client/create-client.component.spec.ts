import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CreateClientComponent } from './create-client.component';
import { ClientService } from '../../core/services/client.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { Client } from '../../shared/models/client.model';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';


describe('CreateClientComponent', () => {
  let component: CreateClientComponent;
  let fixture: ComponentFixture<CreateClientComponent>;
  let clientServiceSpy: jasmine.SpyObj<ClientService>;
  let routerSpy: jasmine.SpyObj<Router>;

  const mockClient: Client = {
    id: '1',
    name: 'John Doe',
    email: 'john@abc.com',
    companyName: 'ABC Corp',
    industry: 'IT',
    address: 'Bangalore',
    primaryName: 'John',
    primaryEmail: 'john@abc.com',
    primaryPhone: '9876543210',
    annualTurnover: 1000000,
    documentsSubmitted: true
  };

  beforeEach(async () => {
    clientServiceSpy = jasmine.createSpyObj('ClientService', ['createClient']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    clientServiceSpy.createClient.and.returnValue(of(mockClient));

    await TestBed.configureTestingModule({
      imports: [CreateClientComponent,NoopAnimationsModule, ],
      providers: [
        { provide: ClientService, useValue: clientServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CreateClientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form on init', () => {
    expect(component.clientForm).toBeDefined();
    expect(component.clientForm.valid).toBeFalse();
  });

  it('should submit form and navigate on success', () => {
    spyOn(window, 'alert');

    component.clientForm.setValue({
      companyName: 'ABC Corp',
      industry: 'IT',
      address: 'Bangalore',
      primaryName: 'John',
      primaryEmail: 'john@abc.com',
      primaryPhone: '9876543210',
      annualTurnover: 1000000,
      documentsSubmitted: true
    });

    component.onSubmit();

    expect(clientServiceSpy.createClient)
      .toHaveBeenCalledWith(component.clientForm.value);

    expect(routerSpy.navigate).toHaveBeenCalledWith(['/rm/clients']);
    expect(window.alert).toHaveBeenCalledWith('Client created successfully');
  });

  it('should show alert on submit error', () => {
    spyOn(window, 'alert');
    clientServiceSpy.createClient.and.returnValue(
      throwError(() => new Error('Create failed'))
    );

    component.clientForm.setValue({
      companyName: 'ABC Corp',
      industry: 'IT',
      address: 'Bangalore',
      primaryName: 'John',
      primaryEmail: 'john@abc.com',
      primaryPhone: '9876543210',
      annualTurnover: 1000000,
      documentsSubmitted: true
    });

    component.onSubmit();

    expect(window.alert).toHaveBeenCalledWith('Failed to create client');
  });

  it('should navigate back to dashboard', () => {
    component.goBackToDashboard();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/rm']);
  });

  it('should cancel and navigate to dashboard', () => {
    component.cancel();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/rm']);
  });

  it('should logout and navigate to login', () => {
    spyOn(localStorage, 'clear');

    component.logout();

    expect(localStorage.clear).toHaveBeenCalled();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
  });
});
