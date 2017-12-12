import Ember from 'ember';

export default Ember.Component.extend({

  tagName: 'button',
  classNames: ['btn', 'btn-default'],
  classNameBindings: ['hidden:hidden'],
//
//  isHidden: Ember.computed('hidden', function() {
//    console.log(this.get('hidden'));
//    return this.get('hidden') ? 'hidden' : undefined;
//  }),

  click(e) {
    this.sendAction();
  }
});
