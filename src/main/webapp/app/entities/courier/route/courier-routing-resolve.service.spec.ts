jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICourier, Courier } from '../courier.model';
import { CourierService } from '../service/courier.service';

import { CourierRoutingResolveService } from './courier-routing-resolve.service';

describe('Service Tests', () => {
  describe('Courier routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CourierRoutingResolveService;
    let service: CourierService;
    let resultCourier: ICourier | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CourierRoutingResolveService);
      service = TestBed.inject(CourierService);
      resultCourier = undefined;
    });

    describe('resolve', () => {
      it('should return ICourier returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCourier = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCourier).toEqual({ id: 123 });
      });

      it('should return new ICourier if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCourier = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCourier).toEqual(new Courier());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCourier = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCourier).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
