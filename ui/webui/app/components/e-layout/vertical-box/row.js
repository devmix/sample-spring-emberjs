import Ember from 'ember';
import StyleableMixin from 'webui/mixins/components/styleable';

const {computed, observer} = Ember;

export default Ember.Component.extend(StyleableMixin, {
  classNameBindings: ['extraClass']
});
