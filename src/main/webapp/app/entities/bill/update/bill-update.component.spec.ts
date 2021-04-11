jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BillService } from '../service/bill.service';
import { IBill, Bill } from '../bill.model';
import { IBasket } from 'app/entities/basket/basket.model';
import { BasketService } from 'app/entities/basket/service/basket.service';

import { BillUpdateComponent } from './bill-update.component';

describe('Component Tests', () => {
  describe('Bill Management Update Component', () => {
    let comp: BillUpdateComponent;
    let fixture: ComponentFixture<BillUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let billService: BillService;
    let basketService: BasketService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BillUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BillUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BillUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      billService = TestBed.inject(BillService);
      basketService = TestBed.inject(BasketService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call idBasket query and add missing value', () => {
        const bill: IBill = { id: 456 };
        const idBasket: IBasket = { id: 37011 };
        bill.idBasket = idBasket;

        const idBasketCollection: IBasket[] = [{ id: 78231 }];
        spyOn(basketService, 'query').and.returnValue(of(new HttpResponse({ body: idBasketCollection })));
        const expectedCollection: IBasket[] = [idBasket, ...idBasketCollection];
        spyOn(basketService, 'addBasketToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ bill });
        comp.ngOnInit();

        expect(basketService.query).toHaveBeenCalled();
        expect(basketService.addBasketToCollectionIfMissing).toHaveBeenCalledWith(idBasketCollection, idBasket);
        expect(comp.idBasketsCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const bill: IBill = { id: 456 };
        const idBasket: IBasket = { id: 77891 };
        bill.idBasket = idBasket;

        activatedRoute.data = of({ bill });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(bill));
        expect(comp.idBasketsCollection).toContain(idBasket);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const bill = { id: 123 };
        spyOn(billService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ bill });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: bill }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(billService.update).toHaveBeenCalledWith(bill);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const bill = new Bill();
        spyOn(billService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ bill });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: bill }));
        saveSubject.complete();

        // THEN
        expect(billService.create).toHaveBeenCalledWith(bill);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const bill = { id: 123 };
        spyOn(billService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ bill });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(billService.update).toHaveBeenCalledWith(bill);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackBasketById', () => {
        it('Should return tracked Basket primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackBasketById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
