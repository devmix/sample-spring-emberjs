import Ember from 'ember';


export default Ember.Controller.extend({

  attributes: [
    {id: 'title', title: 'Title', sortable: true},
    {id: 'isnb13', title: 'ISNB13', sortable: true}
  ],

  actions: {
    onCustomAction(action) {
      if ('e-paging-controls-add' === action) {
        this.transitionToRoute('storage.books.book.new');
      }
    }
  }

});
