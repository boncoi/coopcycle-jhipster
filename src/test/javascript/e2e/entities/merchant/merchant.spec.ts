import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { MerchantComponentsPage, MerchantDeleteDialog, MerchantUpdatePage } from './merchant.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Merchant e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let merchantComponentsPage: MerchantComponentsPage;
  let merchantUpdatePage: MerchantUpdatePage;
  let merchantDeleteDialog: MerchantDeleteDialog;
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

  it('should load Merchants', async () => {
    await navBarPage.goToEntity('merchant');
    merchantComponentsPage = new MerchantComponentsPage();
    await browser.wait(ec.visibilityOf(merchantComponentsPage.title), 5000);
    expect(await merchantComponentsPage.getTitle()).to.eq('coopcycleApp.merchant.home.title');
    await browser.wait(ec.or(ec.visibilityOf(merchantComponentsPage.entities), ec.visibilityOf(merchantComponentsPage.noResult)), 1000);
  });

  it('should load create Merchant page', async () => {
    await merchantComponentsPage.clickOnCreateButton();
    merchantUpdatePage = new MerchantUpdatePage();
    expect(await merchantUpdatePage.getPageTitle()).to.eq('coopcycleApp.merchant.home.createOrEditLabel');
    await merchantUpdatePage.cancel();
  });

  it('should create and save Merchants', async () => {
    const nbButtonsBeforeCreate = await merchantComponentsPage.countDeleteButtons();

    await merchantComponentsPage.clickOnCreateButton();

    await promise.all([
      merchantUpdatePage.setMerchantNameInput('merchantName'),
      merchantUpdatePage.setAddressInput('address'),
      merchantUpdatePage.merchantTypeSelectLastOption(),
      merchantUpdatePage.setImageProfilInput(absolutePath),
      merchantUpdatePage.setMobilePhoneInput('mobilePhone'),
      merchantUpdatePage.userSelectLastOption(),
      merchantUpdatePage.cooperativeSelectLastOption(),
    ]);

    expect(await merchantUpdatePage.getMerchantNameInput()).to.eq(
      'merchantName',
      'Expected MerchantName value to be equals to merchantName'
    );
    expect(await merchantUpdatePage.getAddressInput()).to.eq('address', 'Expected Address value to be equals to address');
    expect(await merchantUpdatePage.getImageProfilInput()).to.endsWith(
      fileNameToUpload,
      'Expected ImageProfil value to be end with ' + fileNameToUpload
    );
    expect(await merchantUpdatePage.getMobilePhoneInput()).to.eq('mobilePhone', 'Expected MobilePhone value to be equals to mobilePhone');

    await merchantUpdatePage.save();
    expect(await merchantUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await merchantComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Merchant', async () => {
    const nbButtonsBeforeDelete = await merchantComponentsPage.countDeleteButtons();
    await merchantComponentsPage.clickOnLastDeleteButton();

    merchantDeleteDialog = new MerchantDeleteDialog();
    expect(await merchantDeleteDialog.getDialogTitle()).to.eq('coopcycleApp.merchant.delete.question');
    await merchantDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(merchantComponentsPage.title), 5000);

    expect(await merchantComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
