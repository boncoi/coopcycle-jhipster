import { element, by, ElementFinder } from 'protractor';

export class AdministratorComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-administrator div table .btn-danger'));
  title = element.all(by.css('jhi-administrator div h2#page-heading span')).first();
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

export class AdministratorUpdatePage {
  pageTitle = element(by.id('jhi-administrator-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  levelOfAuthorityInput = element(by.id('field_levelOfAuthority'));
  imageProfilInput = element(by.id('file_imageProfil'));
  mobilePhoneInput = element(by.id('field_mobilePhone'));

  userSelect = element(by.id('field_user'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setLevelOfAuthorityInput(levelOfAuthority: string): Promise<void> {
    await this.levelOfAuthorityInput.sendKeys(levelOfAuthority);
  }

  async getLevelOfAuthorityInput(): Promise<string> {
    return await this.levelOfAuthorityInput.getAttribute('value');
  }

  async setImageProfilInput(imageProfil: string): Promise<void> {
    await this.imageProfilInput.sendKeys(imageProfil);
  }

  async getImageProfilInput(): Promise<string> {
    return await this.imageProfilInput.getAttribute('value');
  }

  async setMobilePhoneInput(mobilePhone: string): Promise<void> {
    await this.mobilePhoneInput.sendKeys(mobilePhone);
  }

  async getMobilePhoneInput(): Promise<string> {
    return await this.mobilePhoneInput.getAttribute('value');
  }

  async userSelectLastOption(): Promise<void> {
    await this.userSelect.all(by.tagName('option')).last().click();
  }

  async userSelectOption(option: string): Promise<void> {
    await this.userSelect.sendKeys(option);
  }

  getUserSelect(): ElementFinder {
    return this.userSelect;
  }

  async getUserSelectedOption(): Promise<string> {
    return await this.userSelect.element(by.css('option:checked')).getText();
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

export class AdministratorDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-administrator-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-administrator'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
