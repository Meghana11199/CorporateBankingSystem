import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { ClientEditComponent } from './client-edit.component';
import { ClientService } from '../../core/services/client.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { Client } from '../../shared/models/client.model';

describe('ClientEditComponent', () => {
  let component: ClientEditComponent;
  let fixture: ComponentFixture<ClientEditComponent>;
  let clientServiceSpy: jasmine.SpyObj<ClientService>;
  let routerSpy: jasmine.SpyObj<Router>;
  let snackBarSpy: jasmine.SpyObj<MatSnackBar>;

  const mockClient: Client = {
    id: '123',
    name: 'John Doe',
    email: 'john@test.com',
    companyName: 'ABC Corp',
    industry: 'IT',
    address: 'Bangalore',
    primaryName: 'John',
    primaryEmail: 'john@test.com',
    primaryPhone: '9999999999',
    annualTurnover: 100000,
    documentsSubmitted: true
  };

  beforeEach(async () => {
    clientServiceSpy = jasmine.createSpyObj('ClientService', [
      'getClientById',
      'updateClient'
    ]);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);
    snackBarSpy = jasmine.createSpyObj('MatSnackBar', ['open']);

    clientServiceSpy.getClientById.and.returnValue(of(mockClient));

    await TestBed.configureTestingModule({
      imports: [ClientEditComponent, NoopAnimationsModule],
      providers: [
        { provide: ClientService, useValue: clientServiceSpy },
        { provide: Router, useValue: routerSpy },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: { paramMap: { get: () => '123' } }
          }
        }
      ]
    })
      // ✅ CORRECT way to inject snackbar spy for standalone component
      .overrideComponent(ClientEditComponent, {
        set: {
          providers: [{ provide: MatSnackBar, useValue: snackBarSpy }]
        }
      })
      .compileComponents();

    fixture = TestBed.createComponent(ClientEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  function makeFormValid() {
    component.submitting = false;
    component.form.setValue({
      companyName: 'ABC Corp',
      industry: 'IT',
      address: 'Bangalore',
      primaryName: 'John',
      primaryEmail: 'john@test.com',
      primaryPhone: '9999999999',
      annualTurnover: 100000,
      documentsSubmitted: true
    });
  }

  it('should update client successfully', fakeAsync(() => {
    clientServiceSpy.updateClient.and.returnValue(of(mockClient));

    makeFormValid();
    component.updateClient();
    tick();

    expect(snackBarSpy.open).toHaveBeenCalledWith(
      'Client details updated successfully',
      'OK',
      jasmine.any(Object)
    );

    tick(1000);
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/rm/clients']);
  }));

  it('should handle update client error', fakeAsync(() => {
    clientServiceSpy.updateClient.and.returnValue(
      throwError(() => new Error('Update failed'))
    );

    makeFormValid();
    component.updateClient();
    tick();

    expect(snackBarSpy.open).toHaveBeenCalledWith(
      'Failed to update client',
      'Close',
      jasmine.any(Object)
    );
  }));
});
