import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { BillComponentsPage, BillDeleteDialog, BillUpdatePage } from './bill.page-object';

const expect = chai.expect;

describe('Bill e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let billComponentsPage: BillComponentsPage;
  let billUpdatePage: BillUpdatePage;
  let billDeleteDialog: BillDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Bills', async () => {
    await navBarPage.goToEntity('bill');
    billComponentsPage = new BillComponentsPage();
    await browser.wait(ec.visibilityOf(billComponentsPage.title), 5000);
    expect(await billComponentsPage.getTitle()).to.eq('coopcycleApp.bill.home.title');
    await browser.wait(ec.or(ec.visibilityOf(billComponentsPage.entities), ec.visibilityOf(billComponentsPage.noResult)), 1000);
  });

  it('should load create Bill page', async () => {
    await billComponentsPage.clickOnCreateButton();
    billUpdatePage = new BillUpdatePage();
    expect(await billUpdatePage.getPageTitle()).to.eq('coopcycleApp.bill.home.createOrEditLabel');
    await billUpdatePage.cancel();
  });

  it('should create and save Bills', async () => {
    const nbButtonsBeforeCreate = await billComponentsPage.countDeleteButtons();

    await billComponentsPage.clickOnCreateButton();

    await promise.all([
      billUpdatePage.setDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      billUpdatePage.paymentSelectLastOption(),
      billUpdatePage.statusSelectLastOption(),
      billUpdatePage.setTotalPriceInput('5'),
      billUpdatePage.idBasketSelectLastOption(),
    ]);

    expect(await billUpdatePage.getDateInput()).to.contain('2001-01-01T02:30', 'Expected date value to be equals to 2000-12-31');
    expect(await billUpdatePage.getTotalPriceInput()).to.eq('5', 'Expected totalPrice value to be equals to 5');

    await billUpdatePage.save();
    expect(await billUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await billComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Bill', async () => {
    const nbButtonsBeforeDelete = await billComponentsPage.countDeleteButtons();
    await billComponentsPage.clickOnLastDeleteButton();

    billDeleteDialog = new BillDeleteDialog();
    expect(await billDeleteDialog.getDialogTitle()).to.eq('coopcycleApp.bill.delete.question');
    await billDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(billComponentsPage.title), 5000);

    expect(await billComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
