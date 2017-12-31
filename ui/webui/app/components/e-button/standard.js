import Ember from 'ember';

export default Ember.Component.extend({

  tagName: '',
  tabIndex: -1,

  // classNames: ['ui', 'button', 'small'],
  // classNameBindings: ['hidden:hidden', 'hasIcon:icon'],
  // attributeBindings: ['hint'],

  // hasIcon: Ember.computed('icon', function () {
  //   return this.get('icon');
  // }),

  iconClass: Ember.computed('icon', function () {
    return this.get('icon') ? 'icon' : undefined;
  }),

  hiddenClass: Ember.computed('hidden', function () {
    return this.get('hidden') ? 'hidden' : undefined;
  }),

  actions: {
    click() {
      this.sendAction();
    }
  }
});
