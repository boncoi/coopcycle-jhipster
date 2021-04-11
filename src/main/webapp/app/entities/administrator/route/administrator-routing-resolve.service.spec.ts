jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAdministrator, Administrator } from '../administrator.model';
import { AdministratorService } from '../service/administrator.service';

import { AdministratorRoutingResolveService } from './administrator-routing-resolve.service';

describe('Service Tests', () => {
  describe('Administrator routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AdministratorRoutingResolveService;
    let service: AdministratorService;
    let resultAdministrator: IAdministrator | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AdministratorRoutingResolveService);
      service = TestBed.inject(AdministratorService);
      resultAdministrator = undefined;
    });

    describe('resolve', () => {
      it('should return IAdministrator returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAdministrator = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAdministrator).toEqual({ id: 123 });
      });

      it('should return new IAdministrator if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAdministrator = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAdministrator).toEqual(new Administrator());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAdministrator = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAdministrator).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
