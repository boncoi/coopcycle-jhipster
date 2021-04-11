import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CourierService } from '../service/courier.service';

import { CourierComponent } from './courier.component';

describe('Component Tests', () => {
  describe('Courier Management Component', () => {
    let comp: CourierComponent;
    let fixture: ComponentFixture<CourierComponent>;
    let service: CourierService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CourierComponent],
      })
        .overrideTemplate(CourierComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CourierComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CourierService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.couriers?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
