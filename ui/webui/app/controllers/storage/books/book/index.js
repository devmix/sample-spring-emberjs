import Ember from 'ember';


export default Ember.Controller.extend({

  actions: {
    onCustomAction(action) {
      if ('e-paging-controls-add' === action) {
        this.transitionToRoute('storage.books.book.new');
      }
    }
  }

});
