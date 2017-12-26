import {moduleForComponent, test} from 'ember-qunit';
import hbs from 'htmlbars-inline-precompile';

moduleForComponent('e-layout/vertical-box', 'Integration | Component | e layout/vertical box', {
  integration: true
});

test('it renders', function (assert) {
  // Set any properties with this.set('myProperty', 'value');
  // Handle any actions with this.on('myAction', function(val) { ... });

  this.render(hbs`{{e-layout/vertical-box}}`);

  assert.equal(this.$().text().trim(), '');

  // Template block usage:
  this.render(hbs`
    {{#e-layout/vertical-box}}
      template block text
    {{/e-layout/vertical-box}}
  `);

  assert.equal(this.$().text().trim(), 'template block text');
});
