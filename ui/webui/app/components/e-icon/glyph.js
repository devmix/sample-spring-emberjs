import Ember from 'ember';

export default Ember.Component.extend({

  tagName: 'i',
  classNames: ['icon'],
  classNameBindings: ['iconClass', 'link:link'],

  iconClass: Ember.computed('icon', function () {
    return this.get('icon');
  }),

  click() {
    this.sendAction();
  }
});
