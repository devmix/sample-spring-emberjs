import { moduleForComponent, test } from 'ember-qunit';
import hbs from 'htmlbars-inline-precompile';

moduleForComponent('e-tabsheet/tabs-tab', 'Integration | Component | e tabsheet/tabs tab', {
  integration: true
});

test('it renders', function(assert) {
  // Set any properties with this.set('myProperty', 'value');
  // Handle any actions with this.on('myAction', function(val) { ... });

  this.render(hbs`{{e-tabsheet/tabs-tab}}`);

  assert.equal(this.$().text().trim(), '');

  // Template block usage:
  this.render(hbs`
    {{#e-tabsheet/tabs-tab}}
      template block text
    {{/e-tabsheet/tabs-tab}}
  `);

  assert.equal(this.$().text().trim(), 'template block text');
});
