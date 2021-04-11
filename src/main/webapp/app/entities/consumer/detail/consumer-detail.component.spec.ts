import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';

import { ConsumerDetailComponent } from './consumer-detail.component';

describe('Component Tests', () => {
  describe('Consumer Management Detail Component', () => {
    let comp: ConsumerDetailComponent;
    let fixture: ComponentFixture<ConsumerDetailComponent>;
    let dataUtils: DataUtils;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ConsumerDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ consumer: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ConsumerDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ConsumerDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = TestBed.inject(DataUtils);
    });

    describe('OnInit', () => {
      it('Should load consumer on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.consumer).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeBase64, fakeContentType);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeBase64, fakeContentType);
      });
    });
  });
});
