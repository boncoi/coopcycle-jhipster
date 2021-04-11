import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICourier, Courier } from '../courier.model';

import { CourierService } from './courier.service';

describe('Service Tests', () => {
  describe('Courier Service', () => {
    let service: CourierService;
    let httpMock: HttpTestingController;
    let elemDefault: ICourier;
    let expectedResult: ICourier | ICourier[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CourierService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        working: false,
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

      it('should create a Courier', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Courier()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Courier', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            working: true,
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

      it('should partial update a Courier', () => {
        const patchObject = Object.assign(
          {
            working: true,
            imageProfil: 'BBBBBB',
          },
          new Courier()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Courier', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            working: true,
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

      it('should delete a Courier', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCourierToCollectionIfMissing', () => {
        it('should add a Courier to an empty array', () => {
          const courier: ICourier = { id: 123 };
          expectedResult = service.addCourierToCollectionIfMissing([], courier);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(courier);
        });

        it('should not add a Courier to an array that contains it', () => {
          const courier: ICourier = { id: 123 };
          const courierCollection: ICourier[] = [
            {
              ...courier,
            },
            { id: 456 },
          ];
          expectedResult = service.addCourierToCollectionIfMissing(courierCollection, courier);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Courier to an array that doesn't contain it", () => {
          const courier: ICourier = { id: 123 };
          const courierCollection: ICourier[] = [{ id: 456 }];
          expectedResult = service.addCourierToCollectionIfMissing(courierCollection, courier);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(courier);
        });

        it('should add only unique Courier to an array', () => {
          const courierArray: ICourier[] = [{ id: 123 }, { id: 456 }, { id: 30777 }];
          const courierCollection: ICourier[] = [{ id: 123 }];
          expectedResult = service.addCourierToCollectionIfMissing(courierCollection, ...courierArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const courier: ICourier = { id: 123 };
          const courier2: ICourier = { id: 456 };
          expectedResult = service.addCourierToCollectionIfMissing([], courier, courier2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(courier);
          expect(expectedResult).toContain(courier2);
        });

        it('should accept null and undefined values', () => {
          const courier: ICourier = { id: 123 };
          expectedResult = service.addCourierToCollectionIfMissing([], null, courier, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(courier);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
