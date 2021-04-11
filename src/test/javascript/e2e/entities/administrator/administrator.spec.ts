import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { AdministratorComponentsPage, AdministratorDeleteDialog, AdministratorUpdatePage } from './administrator.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Administrator e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let administratorComponentsPage: AdministratorComponentsPage;
  let administratorUpdatePage: AdministratorUpdatePage;
  let administratorDeleteDialog: AdministratorDeleteDialog;
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

  it('should load Administrators', async () => {
    await navBarPage.goToEntity('administrator');
    administratorComponentsPage = new AdministratorComponentsPage();
    await browser.wait(ec.visibilityOf(administratorComponentsPage.title), 5000);
    expect(await administratorComponentsPage.getTitle()).to.eq('coopcycleApp.administrator.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(administratorComponentsPage.entities), ec.visibilityOf(administratorComponentsPage.noResult)),
      1000
    );
  });

  it('should load create Administrator page', async () => {
    await administratorComponentsPage.clickOnCreateButton();
    administratorUpdatePage = new AdministratorUpdatePage();
    expect(await administratorUpdatePage.getPageTitle()).to.eq('coopcycleApp.administrator.home.createOrEditLabel');
    await administratorUpdatePage.cancel();
  });

  it('should create and save Administrators', async () => {
    const nbButtonsBeforeCreate = await administratorComponentsPage.countDeleteButtons();

    await administratorComponentsPage.clickOnCreateButton();

    await promise.all([
      administratorUpdatePage.setLevelOfAuthorityInput('5'),
      administratorUpdatePage.setImageProfilInput(absolutePath),
      administratorUpdatePage.setMobilePhoneInput('mobilePhone'),
      administratorUpdatePage.userSelectLastOption(),
    ]);

    expect(await administratorUpdatePage.getLevelOfAuthorityInput()).to.eq('5', 'Expected levelOfAuthority value to be equals to 5');
    expect(await administratorUpdatePage.getImageProfilInput()).to.endsWith(
      fileNameToUpload,
      'Expected ImageProfil value to be end with ' + fileNameToUpload
    );
    expect(await administratorUpdatePage.getMobilePhoneInput()).to.eq(
      'mobilePhone',
      'Expected MobilePhone value to be equals to mobilePhone'
    );

    await administratorUpdatePage.save();
    expect(await administratorUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await administratorComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Administrator', async () => {
    const nbButtonsBeforeDelete = await administratorComponentsPage.countDeleteButtons();
    await administratorComponentsPage.clickOnLastDeleteButton();

    administratorDeleteDialog = new AdministratorDeleteDialog();
    expect(await administratorDeleteDialog.getDialogTitle()).to.eq('coopcycleApp.administrator.delete.question');
    await administratorDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(administratorComponentsPage.title), 5000);

    expect(await administratorComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
