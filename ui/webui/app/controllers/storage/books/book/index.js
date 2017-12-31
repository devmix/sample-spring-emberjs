import Ember from 'ember';


export default Ember.Controller.extend({

  modelName: 'books-book',
  config: Ember.inject.service('app/config'),

  attributes: [
    {id: 'title', title: 'Title', sortable: true},
    {id: 'isnb13', title: 'ISNB13', sortable: true}
  ],

  actions: {
    onCustomAction(action) {
      if ('e-table-create-new-item' === action) {
        this.transitionToRoute('storage.books.book.new');
      } else if ('e-table-export-items' === action) {
        this.get('exportDialog').show({
          title: 'Export',
          buttons: [{text: 'Close', class: 'primary'}],
          data: {
            modelName: this.get('modelName'),
            filter: this.get('table.filter')
          }
        });
      }
    }
  }

});
