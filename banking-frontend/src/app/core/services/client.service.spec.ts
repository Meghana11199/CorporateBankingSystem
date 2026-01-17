import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';

import { ClientService } from './client.service';
import { Client } from '../../shared/models/client.model';
import { ClientRequest } from '../../shared/models/ClientRequest.model';
import { environment } from '../../../evironments/environment';

describe('ClientService', () => {
  let service: ClientService;
  let httpMock: HttpTestingController;

  const baseUrl = `${environment.apiUrl}/rm/clients`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });

    service = TestBed.inject(ClientService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // ============================
  // createClient()
  // ============================
  it('should create a client', () => {
    const mockClient: Client = {
      id: '1',
      name: 'ABC Corp'
    } as Client;

    service.createClient(mockClient).subscribe(client => {
      expect(client).toEqual(mockClient);
    });

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockClient);

    req.flush(mockClient);
  });

  // ============================
  // getClients()
  // ============================
  it('should fetch all clients', () => {
    const mockClients: Client[] = [
      { id: '1', name: 'ABC Corp' } as Client,
      { id: '2', name: 'XYZ Ltd' } as Client
    ];

    service.getClients().subscribe(clients => {
      expect(clients.length).toBe(2);
      expect(clients).toEqual(mockClients);
    });

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('GET');

    req.flush(mockClients);
  });

  // ============================
  // getClientById()
  // ============================
  it('should fetch client by id', () => {
    const mockClient: Client = {
      id: '1',
      name: 'ABC Corp'
    } as Client;

    service.getClientById('1').subscribe(client => {
      expect(client).toEqual(mockClient);
    });

    const req = httpMock.expectOne(`${baseUrl}/1`);
    expect(req.request.method).toBe('GET');

    req.flush(mockClient);
  });

  // ============================
  // updateClient()
  // ============================
  it('should update a client', () => {
    const payload: ClientRequest = {
      name: 'Updated Corp'
    } as unknown as ClientRequest;

    const updatedClient: Client = {
      id: '1',
      name: 'Updated Corp'
    } as Client;

    service.updateClient('1', payload).subscribe(client => {
      expect(client).toEqual(updatedClient);
    });

    const req = httpMock.expectOne(`${baseUrl}/1`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(payload);

    req.flush(updatedClient);
  });
});
