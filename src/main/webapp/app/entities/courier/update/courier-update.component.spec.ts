jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CourierService } from '../service/courier.service';
import { ICourier, Courier } from '../courier.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ICooperative } from 'app/entities/cooperative/cooperative.model';
import { CooperativeService } from 'app/entities/cooperative/service/cooperative.service';

import { CourierUpdateComponent } from './courier-update.component';

describe('Component Tests', () => {
  describe('Courier Management Update Component', () => {
    let comp: CourierUpdateComponent;
    let fixture: ComponentFixture<CourierUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let courierService: CourierService;
    let userService: UserService;
    let cooperativeService: CooperativeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CourierUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CourierUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CourierUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      courierService = TestBed.inject(CourierService);
      userService = TestBed.inject(UserService);
      cooperativeService = TestBed.inject(CooperativeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const courier: ICourier = { id: 456 };
        const user: IUser = { id: 13820 };
        courier.user = user;

        const userCollection: IUser[] = [{ id: 8136 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ courier });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Cooperative query and add missing value', () => {
        const courier: ICourier = { id: 456 };
        const cooperative: ICooperative = { id: 41270 };
        courier.cooperative = cooperative;

        const cooperativeCollection: ICooperative[] = [{ id: 31204 }];
        spyOn(cooperativeService, 'query').and.returnValue(of(new HttpResponse({ body: cooperativeCollection })));
        const additionalCooperatives = [cooperative];
        const expectedCollection: ICooperative[] = [...additionalCooperatives, ...cooperativeCollection];
        spyOn(cooperativeService, 'addCooperativeToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ courier });
        comp.ngOnInit();

        expect(cooperativeService.query).toHaveBeenCalled();
        expect(cooperativeService.addCooperativeToCollectionIfMissing).toHaveBeenCalledWith(
          cooperativeCollection,
          ...additionalCooperatives
        );
        expect(comp.cooperativesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const courier: ICourier = { id: 456 };
        const user: IUser = { id: 62330 };
        courier.user = user;
        const cooperative: ICooperative = { id: 11845 };
        courier.cooperative = cooperative;

        activatedRoute.data = of({ courier });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(courier));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.cooperativesSharedCollection).toContain(cooperative);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const courier = { id: 123 };
        spyOn(courierService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ courier });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: courier }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(courierService.update).toHaveBeenCalledWith(courier);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const courier = new Courier();
        spyOn(courierService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ courier });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: courier }));
        saveSubject.complete();

        // THEN
        expect(courierService.create).toHaveBeenCalledWith(courier);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const courier = { id: 123 };
        spyOn(courierService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ courier });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(courierService.update).toHaveBeenCalledWith(courier);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackCooperativeById', () => {
        it('Should return tracked Cooperative primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCooperativeById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
