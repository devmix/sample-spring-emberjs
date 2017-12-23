import Ember from 'ember';

export default Ember.Controller.extend({

  fields: undefined,

  actions: {
    commit() {
      this.get('editFrame').commit().then(() => {
        this.transitionToRoute('storage.books.book');
      });
    },

    cancel() {
      this.get('editFrame').cancel().then(() => history.back());
    }
  }
});
