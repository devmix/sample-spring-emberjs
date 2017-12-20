import Ember from 'ember';
import StyleableMixin from '../../../../mixins/components/styleable';
import DialogOwnerMixin from '../../../../mixins/components/e-dialog/modal-frame/owner';

export default Ember.Component.extend(StyleableMixin, DialogOwnerMixin, {

  classNames: ['commons-layout-flex-row'],

  columns: [
    {title:'First Name', id:'firstName', sortable:true},
    {title:'Middle Name', id:'middleName', sortable:true},
    {title:'Last Name', id:'lastName', sortable:true},
    {view: 'e-app/commons/table/cell-entity-buttons', width: '1px'},
  ],

  store: Ember.inject.service(),

  showEditor(model) {
    this.showModalFrameDialog('edit', {
      title: model.get('isNew') ? 'Create' : 'Edit',
      model: model
    }).then(
      () => this.get('table').onRefresh(),
      () => {
        if (model.get('isNew')) {
          model.destroyRecord();
        }
      });
  },

  actions: {
    onCustomAction(action, data) {
      switch(action) {
        case 'paging-controls-add':
          this.showEditor(this.get('store').createRecord('books-author'));
          break;

        case 'cell-button-edit':
          this.showEditor(data.row.model);
          break;

        case 'cell-button-remove':
          this.showModalFrameDialog('remove-confirm')
            .then(() => data.row.model.destroyRecord(), () => {});
          break;
      }
    },

    registerTable(table) {
      this.set('table', table);
    },

    unregisterTable() {
      this.set('table', undefined);
    }
  }

});
