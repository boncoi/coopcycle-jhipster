import { element, by, ElementFinder } from 'protractor';

export class BillComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-bill div table .btn-danger'));
  title = element.all(by.css('jhi-bill div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class BillUpdatePage {
  pageTitle = element(by.id('jhi-bill-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  dateInput = element(by.id('field_date'));
  paymentSelect = element(by.id('field_payment'));
  statusSelect = element(by.id('field_status'));
  totalPriceInput = element(by.id('field_totalPrice'));

  idBasketSelect = element(by.id('field_idBasket'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setDateInput(date: string): Promise<void> {
    await this.dateInput.sendKeys(date);
  }

  async getDateInput(): Promise<string> {
    return await this.dateInput.getAttribute('value');
  }

  async setPaymentSelect(payment: string): Promise<void> {
    await this.paymentSelect.sendKeys(payment);
  }

  async getPaymentSelect(): Promise<string> {
    return await this.paymentSelect.element(by.css('option:checked')).getText();
  }

  async paymentSelectLastOption(): Promise<void> {
    await this.paymentSelect.all(by.tagName('option')).last().click();
  }

  async setStatusSelect(status: string): Promise<void> {
    await this.statusSelect.sendKeys(status);
  }

  async getStatusSelect(): Promise<string> {
    return await this.statusSelect.element(by.css('option:checked')).getText();
  }

  async statusSelectLastOption(): Promise<void> {
    await this.statusSelect.all(by.tagName('option')).last().click();
  }

  async setTotalPriceInput(totalPrice: string): Promise<void> {
    await this.totalPriceInput.sendKeys(totalPrice);
  }

  async getTotalPriceInput(): Promise<string> {
    return await this.totalPriceInput.getAttribute('value');
  }

  async idBasketSelectLastOption(): Promise<void> {
    await this.idBasketSelect.all(by.tagName('option')).last().click();
  }

  async idBasketSelectOption(option: string): Promise<void> {
    await this.idBasketSelect.sendKeys(option);
  }

  getIdBasketSelect(): ElementFinder {
    return this.idBasketSelect;
  }

  async getIdBasketSelectedOption(): Promise<string> {
    return await this.idBasketSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class BillDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-bill-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-bill'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
