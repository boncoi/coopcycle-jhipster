import { element, by, ElementFinder } from 'protractor';

export class ProductComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-product div table .btn-danger'));
  title = element.all(by.css('jhi-product div h2#page-heading span')).first();
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

export class ProductUpdatePage {
  pageTitle = element(by.id('jhi-product-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  productIDInput = element(by.id('field_productID'));
  productNameInput = element(by.id('field_productName'));
  productTypeSelect = element(by.id('field_productType'));
  priceInput = element(by.id('field_price'));
  productImageInput = element(by.id('file_productImage'));

  merchantSelect = element(by.id('field_merchant'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setProductIDInput(productID: string): Promise<void> {
    await this.productIDInput.sendKeys(productID);
  }

  async getProductIDInput(): Promise<string> {
    return await this.productIDInput.getAttribute('value');
  }

  async setProductNameInput(productName: string): Promise<void> {
    await this.productNameInput.sendKeys(productName);
  }

  async getProductNameInput(): Promise<string> {
    return await this.productNameInput.getAttribute('value');
  }

  async setProductTypeSelect(productType: string): Promise<void> {
    await this.productTypeSelect.sendKeys(productType);
  }

  async getProductTypeSelect(): Promise<string> {
    return await this.productTypeSelect.element(by.css('option:checked')).getText();
  }

  async productTypeSelectLastOption(): Promise<void> {
    await this.productTypeSelect.all(by.tagName('option')).last().click();
  }

  async setPriceInput(price: string): Promise<void> {
    await this.priceInput.sendKeys(price);
  }

  async getPriceInput(): Promise<string> {
    return await this.priceInput.getAttribute('value');
  }

  async setProductImageInput(productImage: string): Promise<void> {
    await this.productImageInput.sendKeys(productImage);
  }

  async getProductImageInput(): Promise<string> {
    return await this.productImageInput.getAttribute('value');
  }

  async merchantSelectLastOption(): Promise<void> {
    await this.merchantSelect.all(by.tagName('option')).last().click();
  }

  async merchantSelectOption(option: string): Promise<void> {
    await this.merchantSelect.sendKeys(option);
  }

  getMerchantSelect(): ElementFinder {
    return this.merchantSelect;
  }

  async getMerchantSelectedOption(): Promise<string> {
    return await this.merchantSelect.element(by.css('option:checked')).getText();
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

export class ProductDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-product-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-product'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
