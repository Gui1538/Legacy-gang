import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { ProdutosListaComponent } from './produtos-lista.component';

describe('ProdutosListaComponent', () => {
  let component: ProdutosListaComponent;
  let fixture: ComponentFixture<ProdutosListaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProdutosListaComponent],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(ProdutosListaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
