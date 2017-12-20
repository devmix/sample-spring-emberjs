import Ember from 'ember';

export default Ember.Component.extend({

  tagName: 'span',
  classNames: ['glyphicon'],
  classNameBindings: ['iconClass'],

  iconClass: Ember.computed('icon', function() {
    return 'glyphicon-' + this.get('icon');
  }),

  click(e) {
    this.sendAction();
  }
});
