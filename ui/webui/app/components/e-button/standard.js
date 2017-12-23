import Ember from 'ember';

export default Ember.Component.extend({

  tagName: 'button',
  classNames: ['ui', 'button', 'small'],
  classNameBindings: ['hidden:hidden', 'hasIcon:icon'],

  hasIcon: Ember.computed('icon', function () {
    return this.get('icon');
  }),

  click(e) {
    this.sendAction();
  }
});
