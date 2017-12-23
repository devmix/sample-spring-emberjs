import Ember from 'ember';
import StyleableMixin from 'webui/mixins/components/styleable';

export default Ember.Component.extend(StyleableMixin, {

  classNames: ['commons-layout-flex-row'],

  columns: [],
  modelName: undefined,
  table: undefined,
  removeDialog: undefined,
  editDialog: undefined,

  store: Ember.inject.service(),

  actions: {
    onCustomAction(action, data) {
      switch (action) {
        case 'e-paging-controls-add':
          this.get('editDialog').show({
            title: 'Create',
            data: this.get('store').createRecord(this.get('modelName'))
          });
          break;

        case 'cell-button-edit':
          this.get('editDialog').show({
            title: 'Edit',
            data: data.row.model
          });
          break;

        case 'cell-button-remove':
          this.get('removeDialog').show({
            data: data.row.model
          });
          break;
      }
    },

    onEditDialogAction(action, data) {
      if ('e-dialog-modal-cancel' === action) {
        this.get('editor').cancel().then(() => this.get('editDialog').hide());
      } else if ('e-dialog-modal-accept' === action) {
        this.get('editor').commit().then(() => {
          Ember.sendEvent(this.get('table'), 'refresh');
          this.get('editDialog').hide()
        });
      }
    },

    onRemoveDialogAction(action, model) {
      if ('e-dialog-modal-accept' === action) {
        model.destroyRecord()
      }
    }
  }

});
