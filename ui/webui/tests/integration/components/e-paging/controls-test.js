import { moduleForComponent, test } from 'ember-qunit';
import hbs from 'htmlbars-inline-precompile';

moduleForComponent('e-paging/controls', 'Integration | Component | e paging/controls', {
  integration: true
});

test('it renders', function(assert) {
  // Set any properties with this.set('myProperty', 'value');
  // Handle any actions with this.on('myAction', function(val) { ... });

  this.render(hbs`{{e-paging/controls}}`);

  assert.equal(this.$().text().trim(), '');

  // Template block usage:
  this.render(hbs`
    {{#e-paging/controls}}
      template block text
    {{/e-paging/controls}}
  `);

  assert.equal(this.$().text().trim(), 'template block text');
});
