import { moduleForComponent, test } from 'ember-qunit';
import hbs from 'htmlbars-inline-precompile';

moduleForComponent('layout/vertical-box/row', 'Integration | Component | layout/vertical box/row', {
  integration: true
});

test('it renders', function(assert) {
  // Set any properties with this.set('myProperty', 'value');
  // Handle any actions with this.on('myAction', function(val) { ... });

  this.render(hbs`{{layout/vertical-box/row}}`);

  assert.equal(this.$().text().trim(), '');

  // Template block usage:
  this.render(hbs`
    {{#layout/vertical-box/row}}
      template block text
    {{/layout/vertical-box/row}}
  `);

  assert.equal(this.$().text().trim(), 'template block text');
});
