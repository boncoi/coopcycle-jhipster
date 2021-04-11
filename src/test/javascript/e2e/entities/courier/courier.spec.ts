import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CourierComponentsPage, CourierDeleteDialog, CourierUpdatePage } from './courier.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Courier e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let courierComponentsPage: CourierComponentsPage;
  let courierUpdatePage: CourierUpdatePage;
  let courierDeleteDialog: CourierDeleteDialog;
  const fileNameToUpload = 'logo-jhipster.png';
  const fileToUpload = '../../../../../../src/main/webapp/content/images/' + fileNameToUpload;
  const absolutePath = path.resolve(__dirname, fileToUpload);
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Couriers', async () => {
    await navBarPage.goToEntity('courier');
    courierComponentsPage = new CourierComponentsPage();
    await browser.wait(ec.visibilityOf(courierComponentsPage.title), 5000);
    expect(await courierComponentsPage.getTitle()).to.eq('coopcycleApp.courier.home.title');
    await browser.wait(ec.or(ec.visibilityOf(courierComponentsPage.entities), ec.visibilityOf(courierComponentsPage.noResult)), 1000);
  });

  it('should load create Courier page', async () => {
    await courierComponentsPage.clickOnCreateButton();
    courierUpdatePage = new CourierUpdatePage();
    expect(await courierUpdatePage.getPageTitle()).to.eq('coopcycleApp.courier.home.createOrEditLabel');
    await courierUpdatePage.cancel();
  });

  it('should create and save Couriers', async () => {
    const nbButtonsBeforeCreate = await courierComponentsPage.countDeleteButtons();

    await courierComponentsPage.clickOnCreateButton();

    await promise.all([
      courierUpdatePage.setImageProfilInput(absolutePath),
      courierUpdatePage.setMobilePhoneInput('mobilePhone'),
      courierUpdatePage.userSelectLastOption(),
      courierUpdatePage.cooperativeSelectLastOption(),
    ]);

    const selectedWorking = courierUpdatePage.getWorkingInput();
    if (await selectedWorking.isSelected()) {
      await courierUpdatePage.getWorkingInput().click();
      expect(await courierUpdatePage.getWorkingInput().isSelected(), 'Expected working not to be selected').to.be.false;
    } else {
      await courierUpdatePage.getWorkingInput().click();
      expect(await courierUpdatePage.getWorkingInput().isSelected(), 'Expected working to be selected').to.be.true;
    }
    expect(await courierUpdatePage.getImageProfilInput()).to.endsWith(
      fileNameToUpload,
      'Expected ImageProfil value to be end with ' + fileNameToUpload
    );
    expect(await courierUpdatePage.getMobilePhoneInput()).to.eq('mobilePhone', 'Expected MobilePhone value to be equals to mobilePhone');

    await courierUpdatePage.save();
    expect(await courierUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await courierComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Courier', async () => {
    const nbButtonsBeforeDelete = await courierComponentsPage.countDeleteButtons();
    await courierComponentsPage.clickOnLastDeleteButton();

    courierDeleteDialog = new CourierDeleteDialog();
    expect(await courierDeleteDialog.getDialogTitle()).to.eq('coopcycleApp.courier.delete.question');
    await courierDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(courierComponentsPage.title), 5000);

    expect(await courierComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
