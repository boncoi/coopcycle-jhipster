import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ConsumerComponentsPage, ConsumerDeleteDialog, ConsumerUpdatePage } from './consumer.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Consumer e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let consumerComponentsPage: ConsumerComponentsPage;
  let consumerUpdatePage: ConsumerUpdatePage;
  let consumerDeleteDialog: ConsumerDeleteDialog;
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

  it('should load Consumers', async () => {
    await navBarPage.goToEntity('consumer');
    consumerComponentsPage = new ConsumerComponentsPage();
    await browser.wait(ec.visibilityOf(consumerComponentsPage.title), 5000);
    expect(await consumerComponentsPage.getTitle()).to.eq('coopcycleApp.consumer.home.title');
    await browser.wait(ec.or(ec.visibilityOf(consumerComponentsPage.entities), ec.visibilityOf(consumerComponentsPage.noResult)), 1000);
  });

  it('should load create Consumer page', async () => {
    await consumerComponentsPage.clickOnCreateButton();
    consumerUpdatePage = new ConsumerUpdatePage();
    expect(await consumerUpdatePage.getPageTitle()).to.eq('coopcycleApp.consumer.home.createOrEditLabel');
    await consumerUpdatePage.cancel();
  });

  it('should create and save Consumers', async () => {
    const nbButtonsBeforeCreate = await consumerComponentsPage.countDeleteButtons();

    await consumerComponentsPage.clickOnCreateButton();

    await promise.all([
      consumerUpdatePage.setAddressInput('address'),
      consumerUpdatePage.setImageProfilInput(absolutePath),
      consumerUpdatePage.setMobilePhoneInput('mobilePhone'),
      consumerUpdatePage.userSelectLastOption(),
    ]);

    expect(await consumerUpdatePage.getAddressInput()).to.eq('address', 'Expected Address value to be equals to address');
    expect(await consumerUpdatePage.getImageProfilInput()).to.endsWith(
      fileNameToUpload,
      'Expected ImageProfil value to be end with ' + fileNameToUpload
    );
    expect(await consumerUpdatePage.getMobilePhoneInput()).to.eq('mobilePhone', 'Expected MobilePhone value to be equals to mobilePhone');

    await consumerUpdatePage.save();
    expect(await consumerUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await consumerComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Consumer', async () => {
    const nbButtonsBeforeDelete = await consumerComponentsPage.countDeleteButtons();
    await consumerComponentsPage.clickOnLastDeleteButton();

    consumerDeleteDialog = new ConsumerDeleteDialog();
    expect(await consumerDeleteDialog.getDialogTitle()).to.eq('coopcycleApp.consumer.delete.question');
    await consumerDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(consumerComponentsPage.title), 5000);

    expect(await consumerComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
