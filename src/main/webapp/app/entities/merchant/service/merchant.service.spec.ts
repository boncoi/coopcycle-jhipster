import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { MerchantType } from 'app/entities/enumerations/merchant-type.model';
import { IMerchant, Merchant } from '../merchant.model';

import { MerchantService } from './merchant.service';

describe('Service Tests', () => {
  describe('Merchant Service', () => {
    let service: MerchantService;
    let httpMock: HttpTestingController;
    let elemDefault: IMerchant;
    let expectedResult: IMerchant | IMerchant[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MerchantService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        merchantName: 'AAAAAAA',
        address: 'AAAAAAA',
        merchantType: MerchantType.RESTAURANT,
        imageProfilContentType: 'image/png',
        imageProfil: 'AAAAAAA',
        mobilePhone: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Merchant', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Merchant()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Merchant', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            merchantName: 'BBBBBB',
            address: 'BBBBBB',
            merchantType: 'BBBBBB',
            imageProfil: 'BBBBBB',
            mobilePhone: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Merchant', () => {
        const patchObject = Object.assign({}, new Merchant());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Merchant', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            merchantName: 'BBBBBB',
            address: 'BBBBBB',
            merchantType: 'BBBBBB',
            imageProfil: 'BBBBBB',
            mobilePhone: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Merchant', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMerchantToCollectionIfMissing', () => {
        it('should add a Merchant to an empty array', () => {
          const merchant: IMerchant = { id: 123 };
          expectedResult = service.addMerchantToCollectionIfMissing([], merchant);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(merchant);
        });

        it('should not add a Merchant to an array that contains it', () => {
          const merchant: IMerchant = { id: 123 };
          const merchantCollection: IMerchant[] = [
            {
              ...merchant,
            },
            { id: 456 },
          ];
          expectedResult = service.addMerchantToCollectionIfMissing(merchantCollection, merchant);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Merchant to an array that doesn't contain it", () => {
          const merchant: IMerchant = { id: 123 };
          const merchantCollection: IMerchant[] = [{ id: 456 }];
          expectedResult = service.addMerchantToCollectionIfMissing(merchantCollection, merchant);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(merchant);
        });

        it('should add only unique Merchant to an array', () => {
          const merchantArray: IMerchant[] = [{ id: 123 }, { id: 456 }, { id: 42327 }];
          const merchantCollection: IMerchant[] = [{ id: 123 }];
          expectedResult = service.addMerchantToCollectionIfMissing(merchantCollection, ...merchantArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const merchant: IMerchant = { id: 123 };
          const merchant2: IMerchant = { id: 456 };
          expectedResult = service.addMerchantToCollectionIfMissing([], merchant, merchant2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(merchant);
          expect(expectedResult).toContain(merchant2);
        });

        it('should accept null and undefined values', () => {
          const merchant: IMerchant = { id: 123 };
          expectedResult = service.addMerchantToCollectionIfMissing([], null, merchant, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(merchant);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
