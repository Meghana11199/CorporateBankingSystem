import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';


import { Client } from '../../shared/models/client.model';
import { ClientRequest } from '../../shared/models/ClientRequest.model';
import { environment } from '../../../evironments/environment';

@Injectable({ providedIn: 'root' })
export class ClientService {
  private baseUrl = `${environment.apiUrl}/rm/clients`;

  constructor(private http: HttpClient) {}

  createClient(client: Client) {
    return this.http.post<Client>(this.baseUrl, client);
  }

  getClients() {
    return this.http.get<Client[]>(this.baseUrl);
  }

getClientById(id: string) {
  return this.http.get<Client>(`${this.baseUrl}/${id}`);
}
updateClient(id: string, payload: ClientRequest) {
  return this.http.put<Client>(
    `${this.baseUrl}/${id}`,
    payload
  );
}
}