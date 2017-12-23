import Ember from 'ember';

export default Ember.Mixin.create({

  didInsertElement() {
    this._super(...arguments);
    const action = this.get('register');
    if (action) {
      this.sendAction('register', this, true);
    }
    if (this.hasOwnProperty('bindTo')) {
      this.set('bindTo', this);
    }
  },

  willDestroyElement() {
    this._super(...arguments);
    const action = this.get('register');
    if (action) {
      this.sendAction('register', this, false);
    }
    if (this.hasOwnProperty('bindTo')) {
      this.set('bindTo', undefined);
    }
  }

});
