import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

import { ProdutoService } from './produto.service';

describe('ProdutoService', () => {
  let service: ProdutoService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    service = TestBed.inject(ProdutoService);
    http = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('envia o texto da pesquisa como parâmetro "busca"', () => {
    service.listar('tênis').subscribe();
    const req = http.expectOne((r) => r.url.endsWith('/produtos') && r.params.get('busca') === 'tênis');
    expect(req.request.method).toBe('GET');
    req.flush([]);
  });
});
