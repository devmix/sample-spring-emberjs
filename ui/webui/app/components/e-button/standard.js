import Ember from 'ember';

export default Ember.Component.extend({

  tagName: 'button',
  classNames: ['btn', 'btn-default'],
  classNameBindings: ['hidden:hidden'],

  click(e) {
    this.sendAction();
  }
});
