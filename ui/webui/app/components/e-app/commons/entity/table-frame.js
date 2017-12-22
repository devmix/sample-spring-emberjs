import Ember from 'ember';
import StyleableMixin from 'webui/mixins/components/styleable';
import DialogOwnerMixin from 'webui/mixins/components/e-dialog/modal-frame/owner';

export default Ember.Component.extend(StyleableMixin, DialogOwnerMixin, {

  classNames: ['commons-layout-flex-row'],

  columns: [],
  modelName: undefined,

  store: Ember.inject.service(),

  showEditor(model) {
    this.showModalFrameDialog('edit', {
      title: model.get('isNew') ? 'Create' : 'Edit',
      model: model
    }).then(
      () => this.get('table').onRefresh(),
      () => model.get('isNew') && model.destroyRecord());
  },

  actions: {
    onCustomAction(action, data) {
      switch (action) {
        case 'paging-controls-add':
          this.showEditor(this.get('store').createRecord(this.get('modelName')));
          break;

        case 'cell-button-edit':
          this.showEditor(data.row.model);
          break;

        case 'cell-button-remove':
          this.showModalFrameDialog('remove-confirm')
            .then(() => data.row.model.destroyRecord(), () => {
            });
          break;
      }
    },

    registerTable(table, register) {
      this.set('table', register ? table : undefined);
    }
  }

});
