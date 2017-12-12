import Ember from 'ember';

export default Ember.Component.extend({

  classNames: ['component-layout-vertical-box'],
  attributeBindings: ['computeStyle:style'],

  computeStyle: Ember.computed('padding', function() {
    return Ember.String.htmlSafe(
      'padding:' + this.getWithDefault('padding', 0)
    );
  })
});
