jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ProductService } from '../service/product.service';
import { IProduct, Product } from '../product.model';
import { IMerchant } from 'app/entities/merchant/merchant.model';
import { MerchantService } from 'app/entities/merchant/service/merchant.service';

import { ProductUpdateComponent } from './product-update.component';

describe('Component Tests', () => {
  describe('Product Management Update Component', () => {
    let comp: ProductUpdateComponent;
    let fixture: ComponentFixture<ProductUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let productService: ProductService;
    let merchantService: MerchantService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProductUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ProductUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProductUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      productService = TestBed.inject(ProductService);
      merchantService = TestBed.inject(MerchantService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Merchant query and add missing value', () => {
        const product: IProduct = { id: 456 };
        const merchant: IMerchant = { id: 86845 };
        product.merchant = merchant;

        const merchantCollection: IMerchant[] = [{ id: 79039 }];
        spyOn(merchantService, 'query').and.returnValue(of(new HttpResponse({ body: merchantCollection })));
        const additionalMerchants = [merchant];
        const expectedCollection: IMerchant[] = [...additionalMerchants, ...merchantCollection];
        spyOn(merchantService, 'addMerchantToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ product });
        comp.ngOnInit();

        expect(merchantService.query).toHaveBeenCalled();
        expect(merchantService.addMerchantToCollectionIfMissing).toHaveBeenCalledWith(merchantCollection, ...additionalMerchants);
        expect(comp.merchantsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const product: IProduct = { id: 456 };
        const merchant: IMerchant = { id: 89118 };
        product.merchant = merchant;

        activatedRoute.data = of({ product });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(product));
        expect(comp.merchantsSharedCollection).toContain(merchant);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const product = { id: 123 };
        spyOn(productService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ product });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: product }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(productService.update).toHaveBeenCalledWith(product);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const product = new Product();
        spyOn(productService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ product });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: product }));
        saveSubject.complete();

        // THEN
        expect(productService.create).toHaveBeenCalledWith(product);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const product = { id: 123 };
        spyOn(productService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ product });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(productService.update).toHaveBeenCalledWith(product);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackMerchantById', () => {
        it('Should return tracked Merchant primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackMerchantById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
